package com.back.domain.member.exception

import java.lang.RuntimeException

class NotFoundMemberException (message:String): RuntimeException(message)