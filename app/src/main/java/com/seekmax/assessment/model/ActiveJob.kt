package com.seekmax.assessment.model

import com.seekmax.assessment.ActiveQuery

data class ActiveJob(
    val id: String,
    val positionTitle: String,
    val description: String,
    val industry: Int,
    val haveIApplied: Boolean
)

fun ActiveQuery.Job.toActiveJob() = ActiveJob(
    id = _id,
    positionTitle = positionTitle,
    description = description,
    industry = industry,
    haveIApplied = haveIApplied
)
