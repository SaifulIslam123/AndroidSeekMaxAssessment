package com.seekmax.assessment.model

import com.seekmax.assessment.ActiveQuery

data class ActiveJob(
    val positionTitle: String,
    val description: String,
    val industry: Int,
    val haveIApplied: Boolean
)

fun ActiveQuery.Job.toActiveJob() = ActiveJob(
    positionTitle = positionTitle ?: "",
    description = description ?: "",
    industry = industry ?: 0,
    haveIApplied = haveIApplied ?: false
)
