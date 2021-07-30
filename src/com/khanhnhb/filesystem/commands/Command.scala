package com.khanhnhb.filesystem.commands

import com.khanhnhb.filesystem.filesystem.State

trait Command {

  def apply(state: State): State

}

object Command {

  def emptyCommand: Command = ???

  def incompleteCommand(str: String): Command = ???

  def from(input: String): Command = {
    val tokens: Array[String] = input.split(" ")

    if (tokens.isEmpty) emptyCommand
    else if ("mkdir".equals(tokens(0)))
      if (tokens.length < 2) incompleteCommand("mkdir")
      else new Mkdir(tokens(1))
    new UnknownCommand
  }
}