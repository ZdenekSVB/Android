package cz.pef.va2_2024_petstore.communication


data class Pet(
    val id: Long?,
    val name: String?,
    val category: Category?,
    val photoUrls: List<String>?,
    val tags: List<Tag>?,
    val status: String?
)
