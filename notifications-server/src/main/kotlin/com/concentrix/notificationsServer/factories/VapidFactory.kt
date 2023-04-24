package com.concentrix.notificationsServer.factories

import com.zerodeplibs.webpush.VAPIDKeyPairs
import com.zerodeplibs.webpush.key.PrivateKeySources
import com.zerodeplibs.webpush.key.PublicKeySources
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import java.io.File

@Component
class VapidFactory {

    @Bean
    fun getVapidKeys(
        @Value("\${private.key.file.path}") privateKeyFilePath: String,
        @Value("\${public.key.file.path}") publicKeyFilePath: String
    ) = VAPIDKeyPairs.of(
        PrivateKeySources.ofPEMFile(File(privateKeyFilePath).toPath()),
        PublicKeySources.ofPEMFile(File(publicKeyFilePath).toPath())
    )
}