%%%-------------------------------------------------------------------
%%% @author Khashyap
%%% @copyright (C) 2018, <COMPANY>
%%% @doc
%%%
%%% @end
%%% Created : 09. Jun 2018 8:20 AM
%%%-------------------------------------------------------------------
-module(exchange).
-author("Khashyap").

%% API
-export([start/0, entrypoint/1]).

start() ->
  {ok, Result} = file:consult("calls.txt"),
  entrypoint(Result).


entrypoint(Result) ->
  Sender = names(Result, []),
  thread(Sender),
  io:format("** Calls to be made **\n", []),
  viewdata(Sender, Result),
  call(Sender, Result),
  entry().



entry() ->
  receive
    {initial, From, To, TimeStamp} ->
      io:format("~p received intro message from ~p [~p]\n", [ To, From, TimeStamp]),
      entry();
    {reply, From, To, TimeStamp} ->
      io:format("~p received reply message from ~p [~p]\n", [ To, From, TimeStamp]),
      entry();
    {Pid, stop} ->
      io:format("Process ~p has received no calls for 1 second, ending...\n", [Pid]),
      entry()
  after
    1500 -> io:format("Master has received no replies for 1.5 seconds, ending...\n", [])
  end.



thread([A | Tail]) ->
  Pid = spawn(calling, receivekeys, [self()]),
  register(A, Pid),
  thread(Tail) ;
thread([]) -> ok.


call([], _Result) -> ok;
call([A| Tail], Result) ->
  A ! {self(), proplists:get_value(A, Result, []) },
  call(Tail, Result).


viewdata([], _) -> io:format("\n", []);
viewdata([Key| Tail], Result) ->
  io:format("~p: ~p\n", [Key, proplists:get_value(Key, Result, [])]),
  viewdata(Tail, Result).


names([], Result) -> Result;
names([{Key, _}| Tail], R) ->
  names(Tail, R ++ [Key]).

