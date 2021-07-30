package com.khanhnhb.filesystem.commands

import com.khanhnhb.filesystem.filesystem.State

class UnknownCommand extends Command {

  override def apply(state: State): State =
    state.setMessage("Command not found")
}
