package com.back.domain.member.exception

import java.lang.RuntimeException

class UnchangedMemberDetailsException(message: String) : RuntimeException(message) {

}