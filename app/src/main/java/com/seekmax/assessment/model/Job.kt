package com.seekmax.assessment.model

import com.seekmax.assessment.JobQuery

data class Job(
    val id: String,
    val positionTitle: String,
    val description: String,
    val industry: String,
    val location: String,
    val haveIApplied: Boolean,
    val minSalary: Int,
    val maxSalary: Int
)

fun JobQuery.Job.toJob() = Job(
    id = _id,
    positionTitle = positionTitle,
    description = description,
    industry = industry.toString(),
    location = location.toString(),
    haveIApplied = haveIApplied,
    minSalary = this.salaryRange.min,
    maxSalary = this.salaryRange.max
)

