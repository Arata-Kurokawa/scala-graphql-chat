package graphql.models

import sangria.schema._

trait LongIdentifiable {
  val id: Long
}

object LongIdentifiable {
  val LongIdentifiableType = InterfaceType(
    "LongIdentifiable",
    fields[Unit, LongIdentifiable](Field("id", LongType, resolve = _.value.id))
  )
}

trait StringIdentifiable {
  val id: String
}

object StringIdentifiable {
  val StringIdentifiableType = InterfaceType(
    "Identifiable",
    fields[Unit, StringIdentifiable](
      Field("id", StringType, resolve = _.value.id)
    )
  )
}
