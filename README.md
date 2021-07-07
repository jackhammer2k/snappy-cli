# snappy-cli

Tiny command-line utility to handle snappy compressed streams/files. Similar to usual compression cli tools like gzip.

## Installation

Just download the binary. You do not need a JVM because the utility has been compiled into a native image using GraalVM.

## Usage

    Usage: snappy [OPTIONS] [FILE]
    
    Options:
      -d, --decompress   decompress
      -nf, --non-framed  do not use framed Snappy
      -k, --keep         keep (don't delete) input files
      -h, --help         Show this message and exit

## Examples

    echo "foobar" | snappy | snappy -d 

## License

Apache 2.0