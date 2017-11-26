package com.github.cuzfrog.mytest

import sbt.testing._

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.duration.Deadline
import scala.util.control.NonFatal

private case class TestCase[T](description: String, codeBlock: () => T)

abstract class MyTestSuite {
  private[this] val tests: ArrayBuffer[TestCase[_]] = ArrayBuffer.empty

  protected final def test[T](description: String)(block: => T): Unit = {
    tests += TestCase(description, () => block)
  }

  private[mytest] def run(eventHandler: EventHandler,
                          loggers: Array[Logger])
                         (implicit taskDef: TaskDef): Unit = {
    tests.foreach { testCase =>
      val startTime = Deadline.now
      val event = try {
        loggers.foreach(_.info(s"Test executing:[${testCase.description}]"))
        val result = testCase.codeBlock.apply() //run code
        loggers.foreach(_.info(s"return: $result"))
        MyTestEvent(Status.Success)
      } catch {
        case NonFatal(t) =>
          MyTestEvent(Status.Failure)
      }

      val duration = (Deadline.now - startTime).toMillis
      eventHandler.handle(event.copy(duration = duration): Event)
    }
  }
}