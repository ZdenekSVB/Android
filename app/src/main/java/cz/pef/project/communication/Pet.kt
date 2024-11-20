package cz.pef.project.communication

data class Pet(
    val id: Long?,
    val name: String?,
    val category: Category?,
    val photoUrls: List<String>?,
    val tags: List<Tag>?,
    val status: String?
)
