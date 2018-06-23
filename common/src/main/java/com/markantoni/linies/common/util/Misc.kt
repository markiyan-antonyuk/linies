package com.markantoni.linies.common.util

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

val EXECUTOR: ExecutorService = Executors.newFixedThreadPool(4)