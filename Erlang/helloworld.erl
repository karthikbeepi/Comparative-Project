% hello world program
-module(helloworld).
-export([start/0]).

start() ->
    io:fwrite("Hello, wo1rld!\n").