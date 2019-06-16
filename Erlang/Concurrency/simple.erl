-module(simple).
-export([start/0]).
start() ->
    % register(entry, spawn(?MODULE, entry, [])),
    PID = spawn(simple1, entry, []),
    PID ! {2},
    io:fwrite("hiR3\n")
.