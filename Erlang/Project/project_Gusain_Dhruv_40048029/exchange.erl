 
-module(exchange).

%% API
-export([start/0, master_method/1]).

start() ->
  {ok, Result} = file:consult("calls.txt"),
  master_method(Result).


master_method(Result) ->
  ListIndividual = getName(Result, []),
  createProcessId(ListIndividual),
  io:format("** Calls to be made **\n", []),
  display(ListIndividual, Result),
  sendContactList(ListIndividual, Result),
  masterProcess().



masterProcess() ->
  receive
    {initial, From, To, TimeStamp} ->
      io:format("~p received intro message from ~p [~p]\n", [ To, From, TimeStamp]),
      masterProcess();
    {reply, From, To, TimeStamp} ->
      io:format("~p received reply message from ~p [~p]\n", [ To, From, TimeStamp]),
      masterProcess();
    {Pid, stop} ->
      io:format("Process ~p has received no calls for 1 second, ending...\n", [Pid]),
      masterProcess()
  after
    1500 -> io:format("Master has received no replies for 1.5 seconds, ending...\n", [])
  end.



createProcessId([A | Tail]) ->
  Pid = spawn(calling, processData, [self()]),
  register(A, Pid),
  createProcessId(Tail) ;
createProcessId([]) -> ok.


sendContactList([], _Result) -> ok;
sendContactList([A| Tail], Result) ->
  A ! {self(), proplists:get_value(A, Result, []) },
  sendContactList(Tail, Result).


display([], _) -> io:format("\n", []);
display([Key| Tail], Result) ->
  io:format("~p: ~p\n", [Key, proplists:get_value(Key, Result, [])]),
  display(Tail, Result).


getName([], Result) -> Result;
getName([{Key, _}| Tail], R) ->
  getName(Tail, R ++ [Key]).

