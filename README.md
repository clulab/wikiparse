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

    rsync -avz extracted marcov@filexfer.hpc.arizona.edu:wikiparse --log-file=hpc-user-rsync.log
    scp target/scala-2.12/wikiparse-assembly-0.1.0-SNAPSHOT.jar marcov@filexfer.hpc.arizona.edu:wikiparse
    scp annotator.pbs marcov@filexfer.hpc.arizona.edu:wikiparse

run in hpc

    qsub -J 1-586 annotator.pbs

note that we need 586 workers, one per input dir. you can verify this number

    ls extracted | wc -l

you can verify the job is running using qstat

more info here: https://docs.hpc.arizona.edu/display/UAHPC/Running+Jobs
