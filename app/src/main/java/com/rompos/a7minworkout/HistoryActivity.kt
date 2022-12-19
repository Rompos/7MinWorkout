package com.rompos.a7minworkout


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.rompos.a7minworkout.databinding.ActivityHistoryBinding
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {

    private var binding: ActivityHistoryBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setSupportActionBar(binding?.toolbarHistoryActivity)

        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "History"
        }

        binding?.toolbarHistoryActivity?.setNavigationOnClickListener {
            //onBackPressed() ////since i use custom Dialog for back button also deprecated in version 33 and above
            onBackPressedDispatcher.onBackPressed()
        }

        val historyDao = (application as WorkOutApp).db.historyDao()
        //getAllCompletedDates(dao)

        lifecycleScope.launch {
            historyDao.fetchAllDates().collect {
                Log.d("exact-history", "$it")
                val list = ArrayList(it)
                setupListOfDataIntoRecyclerView(list, historyDao)
            }
        }
    }

    /*private fun getAllCompletedDates(historyDao: HistoryDao){
        Log.e("Date : ","getAllCompletedDates run")
        lifecycleScope.launch{
            historyDao.fetchAllDates().collect { allCompletedDatesList ->
                if(allCompletedDatesList.isNotEmpty()){
                    binding?.tvHistory?.visibility = View.VISIBLE
                    binding?.rvHistory?.visibility = View.VISIBLE
                    binding?.tvNoDataAvailable?.visibility = View.INVISIBLE

                    binding?.rvHistory?.layoutManager = LinearLayoutManager(this@HistoryActivity)

                    val dates = ArrayList<String>()
                    for(date in allCompletedDatesList){
                        dates.add(date.date)
                    }
                    val historyAdapter = HistoryAdapter(dates)

                    binding?.rvHistory?.adapter = historyAdapter
                    setupListOfDataIntoRecyclerView(dates,historyDao)
                }else{
                    binding?.tvHistory?.visibility = View.GONE
                    binding?.rvHistory?.visibility = View.GONE
                    binding?.tvNoDataAvailable?.visibility = View.VISIBLE
                }
                ////just for logcat testing
                *//*for(i in allCompletedDatesList){
                    Log.e("Date : "," "+i.date)
                }*//*
            }
        }
    }*/

    ////--------------------Either this or the above ????---------------------------
    private fun setupListOfDataIntoRecyclerView(
        historyList: ArrayList<HistoryEntity>,
        historyDao: HistoryDao
    ) {
        if (historyList.isNotEmpty()) {
            val historyAdapter = HistoryAdapter(historyList)
            { deleteItem ->
                deleteRecordAlertDialog(deleteItem, historyDao)
            }

            binding?.rvHistory?.layoutManager = LinearLayoutManager(this)
            binding?.rvHistory?.adapter = historyAdapter
            binding?.rvHistory?.visibility = View.VISIBLE
            binding?.tvNoDataAvailable?.visibility = View.GONE

        } else {
            binding?.rvHistory?.visibility = View.GONE
            binding?.tvNoDataAvailable?.visibility = View.VISIBLE
        }
    }
    ////-----------------------------------------------------------------------------

    /*private fun deleteRecordAlertDialog(id : Int ,historyDao: HistoryDao,history: HistoryEntity) {
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle("Delete Record")

        builder.setIcon(android.R.drawable.ic_dialog_alert)
        lifecycleScope.launch {
            historyDao.fetchHistoryById(id).collect {
                if (it != null) {//set message for alert dialog
                    builder.setMessage("Are you sure you wants to delete ${history.date}.")
                }
            }
        }
        //performing positive action
        builder.setPositiveButton("Yes") { dialogInterface, _ ->
            lifecycleScope.launch{
                historyDao.delete(HistoryEntity(id))
                //Toast.makeText(applicationContext,"Record deleted Successfully !",Toast.LENGTH_LONG).show()
            }
            dialogInterface.dismiss()
        }
        //performing negative action
        builder.setNegativeButton("No") { dialogInterface, _ ->
            dialogInterface.dismiss() // Dialog will be dismissed
        }
                // Create the AlertDialog
                val alertDialog: AlertDialog = builder.create()
                // Set other dialog properties
                alertDialog.setCancelable(false) // Will not allow user to cancel after clicking on remaining screen area.
                alertDialog.show()  // show the dialog to UI
    }*/

    private fun deleteRecordAlertDialog(historyEntity: HistoryEntity, historyDao: HistoryDao) {
        val builder = AlertDialog.Builder(this).apply {
            setTitle("Delete Record")
            setMessage("Are you sure you wants to delete :\n${historyEntity.date}.")
            setIcon(android.R.drawable.ic_dialog_alert)
            setPositiveButton("Yes") { dialog, _ ->
                lifecycleScope.launch {
                    historyDao.delete(historyEntity)

                    Toast.makeText(
                        applicationContext,
                        "Record deleted successfully.",
                        Toast.LENGTH_LONG
                    ).show()

                    dialog.dismiss()
                }
            }
            setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false) // Will not allow user to cancel after clicking on remaining screen area.
        alertDialog.show()  // show the dialog to UI
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}