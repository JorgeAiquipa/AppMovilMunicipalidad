# AppMovilMunicipalidad
Fuentes del aplicativo Movil

Paso 1 : Municipalidad -> Proyecto en Android Studio

Abrir el proyecto con el Android Studio

    
Dependencias que se iran instalando segun se compile el proyecto .

    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation 'com.android.support:appcompat-v7:26.1.0'
    implementation 'com.android.support.constraint:constraint-layout:1.0.2'
    testImplementation 'junit:junit:4.12'
    androidTestImplementation 'com.android.support.test:runner:1.0.1'
    androidTestImplementation 'com.android.support.test.espresso:espresso-core:3.0.1'
    compile 'com.android.support:design:26.1.0'
    compile 'com.android.support:cardview-v7:26.1.0'
    compile project(path: ':volley')
