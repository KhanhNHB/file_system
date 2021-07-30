package com.khanhnhb.filesystem.filesystem

import com.khanhnhb.filesystem.commands.Command
import com.khanhnhb.filesystem.file.Directory

import java.util.Scanner

object FileSystem extends App {

  val root = Directory.ROOT
  var state = State(root, root)
  val scanner = new Scanner(System.in)

  while (true) {
    state.show
    val input = scanner.nextLine()
    state = Command.from(input).apply(state)
  }

}
