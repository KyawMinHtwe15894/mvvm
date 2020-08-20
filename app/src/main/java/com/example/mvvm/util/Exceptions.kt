package com.example.mvvm.util

import java.io.IOException

class ApiExceptions(message: String): IOException(message)
class NoInternetExceptions(message: String): IOException(message)