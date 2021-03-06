# wikiparse

parse wikipedia on the hpc

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
    rsync -avz extracted marcov@filexfer.hpc.arizona.edu:. --log-file=hpc-user-rsync.log

run in hpc

    qsub -J 1-586 annotator.pbs

note that we need 586 workers, one per input dir. you can verify this number

    ls extracted | wc -l

you can verify the job is running using `qstat`.
you can cancel a job with `qdel`.

you can use `va` to consult the remaining number of hours you have.

more info here: https://docs.hpc.arizona.edu/display/UAHPC/Running+Jobs

## Notes

[PBS Script Assistant](https://jobbuilder.hpc.arizona.edu/)
