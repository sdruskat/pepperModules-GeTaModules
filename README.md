# pepperModules-GeTaModules

A module for the [Pepper](http://corpus-tools.org/pepper/) conversion framework 
for linguistic data.

The module provides an importer for the custom JSON data written by the GeTa 
annotation tool as used in the [TraCES](https://www.traces.uni-hamburg.de/) 
research project.

It can be used to import GeTa data and subsequently manipulate it and export it
to a different format with Pepper.

*GeTaModules* is also provided in a custom GUI for conversion into the ANNIS
format: [Pepper Grinder](https://github.com/sdruskat/pepper-grinder).

## Requirements

- Java >= 1.7
- Pepper ([download](http://corpus-tools.org/pepper/#download), 
[documentation](http://corpus-tools.org/pepper/#documentation)) (module has 
been tested with Pepper v3.1.0)

## Install module

1. Start Pepper (Windows: `pepperStart.bat`, Linux: `bash pepperStart.sh`)
1. `pepper> update de.uni-hamburg::pepperModules-GeTaModules::http://central.maven.org/maven2/`

You can also download the module executable from 
[Maven Central](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22de.uni-hamburg.traces%22%20AND%20a%3A%22pepperModules-GeTaModules%22) and add the path to the JAR file to the configuration
file `conf/pepper.properties`:

`pepper.dropin.paths=/path/to/the/module/jar`

Pepper will then pick it up automatically.

## Usage

To use the module, either use the conversion wizard in Pepper (started by
the `convert`/`c` sommand without parameters), or create a 
[Pepper workflow file](http://corpus-tools.org/pepper/userGuide.html#workflow_file).

In the workflow:

### Identify the module by name

```xml
<importer name="GeTaImporter" path="PATH_TO_CORPUS"/>
```

### Properties

`GeTaModules` does not provide any custom properties.

## Contribute

The project is happy to receive issue reports and pull requests. Please [open a
new issue with your idea or 
report](https://github.com/sdruskat/pepperModules-GeTaModules/issues/new). 
Many thanks.

## Build

To build locally, run `mvn clean install`.

## Deploy

Run

1. `mvn license:format`
1. `mvn site:site`
1. `mvn release:prepare`
1. `mvn release:perform`

## License

```
Copyright 2016ff. Stephan Druskat
All exploitation rights belong exclusively to Universität Hamburg.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```