package com.khanhnhb.filesystem.commands

import com.khanhnhb.filesystem.filesystem.State

class Pwd extends Command {

  override def apply(state: State): State =
    state.setMessage(state.wd.path)

}
