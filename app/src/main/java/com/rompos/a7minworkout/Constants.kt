package com.rompos.a7minworkout

object Constants {

    fun defaultExerciseList(): ArrayList<ExerciseModel>{
        val exerciseList = ArrayList<ExerciseModel>()
    //-------------------Start of Exercises -------------------------------
        val sitUps = ExerciseModel(
            1,
            "Sit Ups",
            R.drawable.ic_sit_ups,
            false,
            false
        )

        exerciseList.add(sitUps)

        val highKneesRunningInPlace = ExerciseModel(
            2,
            "High Knees Running In Place",
            R.drawable.ic_high_knees_running_in_place,
            false,
            false
        )

        exerciseList.add(highKneesRunningInPlace)

        val jumpingJacks = ExerciseModel(
            3,
            "Jumping Jacks",
            R.drawable.ic_jumping_jacks,
            false,
            false
        )

        exerciseList.add(jumpingJacks)

        val lunge = ExerciseModel(
            4,
            "Lunge",
            R.drawable.ic_lunge,
            false,
            false
        )

        exerciseList.add(lunge)

        val plank = ExerciseModel(
            5,
            "Plank",
            R.drawable.ic_plank,
            false,
            false
        )

        exerciseList.add(plank)

        val pushUps = ExerciseModel(
            6,
            "Push Ups",
            R.drawable.ic_push_ups,
            false,
            false
        )

        exerciseList.add(pushUps)

        val pushUpAndRotation = ExerciseModel(
            7,
            "Push Up and Rotation",
            R.drawable.ic_push_up_and_rotation,
            false,
            false
        )

        exerciseList.add(pushUpAndRotation)

        val sidePlank = ExerciseModel(
            8,
            "Side Plank",
            R.drawable.ic_side_plank,
            false,
            false
        )

        exerciseList.add(sidePlank)

        val squat = ExerciseModel(
            9,
            "Squat",
            R.drawable.ic_squat,
            false,
            false
        )

        exerciseList.add(squat)

        val stepUpOntoChair = ExerciseModel(
            10,
            "Step Up Onto Chair",
            R.drawable.ic_step_up_onto_chair,
            false,
            false
        )

        exerciseList.add(stepUpOntoChair)

        val tricepsDipOnChair = ExerciseModel(
            11,
            "Triceps Dip on Chair",
            R.drawable.ic_triceps_dip_on_chair,
            false,
            false
        )

        exerciseList.add(tricepsDipOnChair)

        val wallSit = ExerciseModel(
            12,
            "Wall Sit",
            R.drawable.ic_wall_sit,
            false,
            false
        )

        exerciseList.add(wallSit)
    //--------------------End of Exercises----------------------------------
        return exerciseList
    }

}