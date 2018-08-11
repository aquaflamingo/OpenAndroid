package com.pressurelabs.hibernate.data.models

open class PhotoDTO {
        var id: Int = 0
        var image_url: String? = null
        var name: String? = null
        var user: UserDTO? = null
}