package org.jackhammer2k.snappy.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import org.xerial.snappy.SnappyFramedInputStream
import org.xerial.snappy.SnappyFramedOutputStream
import org.xerial.snappy.SnappyInputStream
import org.xerial.snappy.SnappyOutputStream
import java.io.File
import java.io.InputStream
import java.io.OutputStream

class Snappy : CliktCommand() {
    val fileExtension = "snappy"

    val decompress by option("-d", "--decompress", help = "decompress").flag(default = false)
    val nonFramed by option("-nf", "--non-framed", help = "do not use framed Snappy").flag(default = false)
    val keep by option("-k", "--keep", help = "keep (don't delete) input files").flag(default = false)
    val file by argument().file(exists = true).optional()

    override fun run() {
        val file = file
        if (file != null && decompress && file.extension != fileExtension) {
            throw IllegalStateException("file extension has to be \"$fileExtension\", but was ${file.extension}")
        }

        val input = input(file)
        val output = output(file)

        input.use { inputStream -> output.use { outputStream -> inputStream.transferTo(outputStream) } }

        if (!keep) {
            file?.delete()
        }
    }

    internal fun input(file: File?): InputStream = when {
        decompress && nonFramed && file == null -> SnappyInputStream(System.`in`)
        decompress && !nonFramed && file == null -> SnappyFramedInputStream(System.`in`)
        decompress && nonFramed && file != null -> SnappyInputStream(file.inputStream())
        decompress && !nonFramed && file != null -> SnappyFramedInputStream(file.inputStream())
        !decompress && file != null -> file.inputStream()
        !decompress && file == null -> System.`in`
        else -> throw IllegalStateException("unsupported combination of params/arguments")
    }

    internal fun output(file: File?): OutputStream = when {
        decompress && file != null -> file.resolveSibling(file.nameWithoutExtension).outputStream()
        decompress && file == null -> System.out
        !decompress && nonFramed && file == null -> SnappyOutputStream(System.out)
        !decompress && !nonFramed && file == null -> SnappyFramedOutputStream(System.out)
        !decompress && nonFramed && file != null -> SnappyOutputStream(file.resolveSibling("${file.name}.$fileExtension").outputStream())
        !decompress && !nonFramed && file != null -> SnappyFramedOutputStream(file.resolveSibling("${file.name}.$fileExtension").outputStream())
        else -> throw IllegalStateException("unsupported combination of params/arguments")
    }
}

fun main(args: Array<String>) {
    Snappy().main(args)
}
