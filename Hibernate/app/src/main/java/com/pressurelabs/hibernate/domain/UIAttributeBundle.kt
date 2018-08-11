package com.pressurelabs.hibernate.domain

class UIAttributeBundle {
    var text:String?=null
    var color:Int?=null
    var drawableRes:Int?=null

    class Builder(private val mAttribute: UIAttributeBundle = UIAttributeBundle()) {
        fun text(text:String):Builder {
            mAttribute.text=text
            return this
        }

        fun color(color:Int):Builder {
            mAttribute.color=color
            return this
        }

        fun drawable(res:Int):Builder {
            mAttribute.drawableRes=res;
            return this
        }

        fun build(): UIAttributeBundle {
            return mAttribute
        }
    }
}