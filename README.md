## prepare data

download wikipedia dump:

    wget http://dumps.wikimedia.org/enwiki/latest/enwiki-latest-pages-articles.xml.bz2

use wikipedia extractor script from https://github.com/bwbaugh/wikipedia-extractor
to extract docs into the `extracted` directory

    bzcat enwiki-latest-pages-articles.xml.bz2 |  python WikiExtractor.py -cb 250K -o extracted -

## run locally

read documents from `extracted` directory and save results in `output` directory:

    sbt 'runMain org.clulab.wikiparse.ParseDocuments extracted output'

## hpc

package the project into a single fat jar:

    sbt assembly

upload to hpc

    scp target/scala-2.12/wikiparse-assembly-0.1.0-SNAPSHOT.jar marcov@filexfer.hpc.arizona.edu:.
    scp annotator.pbs marcov@filexfer.hpc.arizona.edu:.
    rsync -avz extracted marcov@filexfer.hpc.arizona.edu:wikiparse --log-file=hpc-user-rsync.log

run in hpc

    qsub annotator.pbs

you can verify the job is running using qstat

more info here: https://docs.hpc.arizona.edu/display/UAHPC/Running+Jobs

## TODO

cambiar PBS queue de debug a windfall
walltime de 10 minutos a 100 horas
numero de workers
array de trabajos -- para distribuir input por medio de un indice
