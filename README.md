# doitnow

DoItDoItNow! is a simple to-do list manager, conceived & built as a sample project to go with a series of blog entries on creating
RESTful APIs and web/mobile applications using Clojure.

## Installation

Clone the project, from the project folder run a Ring server with `lein ring server`.
Start using the API at http://localhost:3000/api. Start with an HTTP OPTIONS call to that URL and see how far you get.

## Examples

You can hit the API directly from a command line with:

`curl -i -X OPTIONS -H "Accept: application/json" http://localhost:3000/api`

## License

Copyright © 2012 Paul Umbers, 1622878 Alberta Ltd

Distributed under the Eclipse Public License, the same as Clojure.
