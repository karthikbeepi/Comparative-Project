%%%-------------------------------------------------------------------
%%% @author Khashyap
%%% @copyright (C) 2018, <COMPANY>
%%% @doc
%%%
%%% @end
%%% Created : 09. Jun 2018 8:20 AM
%%%-------------------------------------------------------------------
-module(calling).
-author("Khashyap").

%% API
-export([receivekeys/1]).

receivekeys(Master) ->
  {_, NamePid} = erlang:process_info(self(), registered_name),

  receive
    {Master,  Contacts} ->
      connect(NamePid, Contacts),
      receivekeys(Master);

    {NamePidFrom, initial, TimeStamp} ->
      Master ! {initial, NamePidFrom, NamePid, TimeStamp},

      NamePidFrom ! {NamePid, reply, TimeStamp},

      receivekeys(Master);

    {NamePidFrom, reply, TimeStamp} ->
      Master ! {reply, NamePidFrom, NamePid, TimeStamp},
      receivekeys(Master)
  after
    1000 -> Master ! {NamePid, stop}
  end.

connect(_,[]) -> ok;
connect(NamePid, [Person | Tail]) ->
  {_, _, TimeStamp} = erlang:now(),
  Person ! {NamePid, initial, TimeStamp},
  connect(NamePid , Tail).


