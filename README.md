download wikipedia dump:

    curl -O http://dumps.wikimedia.org/enwiki/latest/enwiki-latest-pages-articles.xml.bz2

use wikipedia extractor script from https://github.com/bwbaugh/wikipedia-extractor
to extract docs into the `extracted` directory

    bzcat enwiki-latest-pages-articles.xml.bz2 |  python WikiExtractor.py -cb 250K -o extracted -
