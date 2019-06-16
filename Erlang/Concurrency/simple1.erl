-module(simple1).
-export([entry/0]).

entry() ->
  receive
    {X} -> io:fwrite("hiR1\n"),
    entry()
  after
    1000 -> io:fwrite("hiR4\n")
  end,
  io:fwrite("hiR2\n").
