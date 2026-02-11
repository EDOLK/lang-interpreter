A general purpose interpreted programming language. Interpreter written in java.
Statically typed.

#### Timeline:
* 02/18:
* Initialize and set primitives
* Basic arithmetic
* 02/25:
* Initialize and set functions, tables, arrays
* 03/04:
* Basic control flow (if, if-else, while and for loops)
    * 03/11:
    * "main" method, read stdin, write to stdout
...

#### example code:
```
declaring primitives
uint32 x;
uint64 y = 12;
string name = "joe";

functions:
can be defined using a signature:
function name(type argument, type argument, ...) : return type
can be bound in tables like regular values

function add(uint32, uint32) : uint32 = (x, y) {
   return x + y;
}

print(add(5, 12));

function addEmpty(uint32, uint32) : uint32;

addEmpty = (x, y) {
    return x + y;
}

tables:
string to value map with unique keys
all keys are either public or private (private by default)

table person = {
    uint32 age = 30;
    string name = "john";
    public function doSomething() : void;
    public function sayHello() : void = () {
        print("hello");
    }
}

person.sayHello();
```

