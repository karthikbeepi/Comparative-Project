
-module(calling).

%% API
-export([processData/1]).

processData(Master) ->
  {_, NamePid} = erlang:process_info(self(), registered_name),

  receive
    {Master,  Contacts} ->
      callFriend(NamePid, Contacts),
      processData(Master);

    {NamePidFrom, initial, TimeStamp} ->
      Master ! {initial, NamePidFrom, NamePid, TimeStamp},

      NamePidFrom ! {NamePid, reply, TimeStamp},

      processData(Master);

    {NamePidFrom, reply, TimeStamp} ->
      Master ! {reply, NamePidFrom, NamePid, TimeStamp},
      processData(Master)
  after
    1000 -> Master ! {NamePid, stop}
  end.

callFriend(_,[]) -> ok;
callFriend(NamePid, [Person | Tail]) ->
  {_, _, TimeStamp} = erlang:now(),
  Person ! {NamePid, initial, TimeStamp},
  callFriend(NamePid , Tail).


