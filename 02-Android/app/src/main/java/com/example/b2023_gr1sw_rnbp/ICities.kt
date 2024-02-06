package com.example.b2023_gr1sw_rnbp

class ICities(
    public var name:String?,
    public var state:String?,
    public var country:String?,
    public var capital:Boolean?,
    public var population: Long?,
    public var regions:ArrayList<String>?,
    ){
    override fun toString(): String {
        return "${name} - ${country}"
    }
}