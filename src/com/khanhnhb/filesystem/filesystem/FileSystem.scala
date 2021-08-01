package com.khanhnhb.filesystem.filesystem

import com.khanhnhb.filesystem.commands.Command
import com.khanhnhb.filesystem.file.Directory

import java.util.Scanner

object FileSystem extends App {

  val root = Directory.ROOT

  io.Source.stdin.getLines().foldLeft(State(root, root))((currentState, newLine) => {
    currentState.show
    Command.from(newLine).apply(currentState)
  })
}