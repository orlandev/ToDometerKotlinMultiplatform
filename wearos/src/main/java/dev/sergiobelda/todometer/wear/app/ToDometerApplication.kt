/*
 * Copyright 2021 Sergio Belda
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.sergiobelda.todometer.wear.app

import dev.sergiobelda.todometer.common.app.ToDometerBaseApplication
import dev.sergiobelda.todometer.common.di.initKoin
import dev.sergiobelda.todometer.wear.di.viewModelModule
import org.koin.android.ext.koin.androidContext

class ToDometerApplication : ToDometerBaseApplication() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            modules(viewModelModule)
            androidContext(this@ToDometerApplication)
        }
    }
}
