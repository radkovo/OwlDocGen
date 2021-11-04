# OwlDocGen
(c) 2021 Radek Burget (burgetr@fit.vutbr.cz)

OwlDocGen generates ontology documentation from OWL ontology definitions. Compared to similar well-known tools such as [LODE](https://github.com/essepuntato/LODE) or [pyLODE](https://github.com/RDFLib/pyLODE), it focuses on generating documentation for multiple interlinked ontologies. See the [FitLayout ontology documentation](http://fitlayout.github.io/ontology/) for an example.

## Installation

OwlDocGen is distributed as platform-independent runnable jar archive:

- Download the runnable archive `OwlDocGen.jar` from [Releases](https://github.com/radkovo/OwlDocGen/releases)
- Run the following command in the command prompt (depending on your operating system):\
  `java -jar OwlDocGen.jar`

You should see a list of available parametres.

## Usage

RDF4J Class Builder takes an OWL definition file in any format (RDF/XML, Turtle, ...) as the input. Multiple input files may be specified that are joined together before processing. The following documents are generated:

- A separate document for every ontology found in the input files
- An index page containing a list of all discovered ontologies.

The following command line parameters may be used:

```
OwlDocGen [-hV] [-o=<path>] [-t=title] [-p=<prefix::iri>[,<prefix::iri>...]]... <filename>...
      <filename>...   Input OWL files
  -h, --help          Show this help message and exit.
  -o, --ouptut-folder=<path>
                      Ouput folder path
  -p, --prefix=<prefix::iri>[,<prefix::iri>...]
                      Prefix definitions
  -t, --title=title   Index page title
  -V, --version       Print version information and exit.

```

Example usage:

```shell
java -jar OwlDocGen.jar \
    -p fl::http://fitlayout.github.io/ontology/fitlayout.owl# \
    -p box::http://fitlayout.github.io/ontology/render.owl# \
    -p segm::http://fitlayout.github.io/ontology/segmentation.owl# \
    -o ontology/doc \
    -t "FitLayout Ontologies" \
    fitlayout.owl \
    render.owl \
    segmentation.owl
```

## Building

OwlDocGen may be build from the sources by maven. After cloning the source repository, use `mvn package` for building and packaging all the components.
