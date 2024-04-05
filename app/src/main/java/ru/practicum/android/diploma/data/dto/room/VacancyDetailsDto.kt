package ru.practicum.android.diploma.data.dto.room

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.data.dto.fields.AddressDto
import ru.practicum.android.diploma.data.dto.fields.ContactsDto
import ru.practicum.android.diploma.data.dto.fields.CountryDto
import ru.practicum.android.diploma.data.dto.fields.EmployerDto
import ru.practicum.android.diploma.data.dto.fields.SalaryDto
import ru.practicum.android.diploma.data.dto.fields.SkillDto
import ru.practicum.android.diploma.data.dto.fields.VacancyElementDto

data class VacancyDetailsDto(
    val id: String,
    @SerializedName("alternate_url")
    val url: String,
    val name: String,
    val area: CountryDto,
    val salary: SalaryDto?,
    val experience: VacancyElementDto?,
    val schedule: VacancyElementDto,
    val contacts: ContactsDto?,
    val address: AddressDto?,
    val employer: EmployerDto?,
    val employment: VacancyElementDto?,
    @SerializedName("key_skills")
    val keySkills: List<SkillDto>?,
    val description: String
)
