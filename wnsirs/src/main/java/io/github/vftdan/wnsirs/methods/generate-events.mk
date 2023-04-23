#! /usr/bin/env -S make -f

HEAD_UPPERCASE=$(shell printf '%s' ${CLASS_NAME} | sed -E 's/^(.).*/\1/' )
HEAD_LOWERCASE=$(shell printf '%s' ${HEAD_UPPERCASE} | tr A-Z a-z )
TAIL=$(shell printf '%s' ${CLASS_NAME} | sed -E 's/^.(.*)/\1/' )
TArgs=Void
TRet=Void
DEFINITIONS= -D HEAD_UPPERCASE=${HEAD_UPPERCASE} -D HEAD_LOWERCASE=${HEAD_LOWERCASE} -D TAIL=${TAIL} -D TArgs=${TArgs} -D TRet=${TRet} -D CLASS_NAME=${HEAD_UPPERCASE}${TAIL} -D HANDLER_NAME=\"${HEAD_LOWERCASE}${TAIL}\" -D EMITTER_NAME=\"emit${HEAD_UPPERCASE}${TAIL}\"

all: IncreasePheromone.java DestroyAnt.java AntCloned.java

IncreasePheromone.java: event_method_IncreasePheromone
DestroyAnt.java: event_method_DestroyAnt
AntCloned.java: event_method_AntCloned
FilterNeighbors.java: TArgs="Set<Node>"
FilterNeighbors.java: event_method_FilterNeighbors
EdgeCreated.java: TArgs="Edge"
EdgeCreated.java: event_method_EdgeCreated

event_method_%: CLASS_NAME = $(patsubst event_method_%,%,$*)
event_method_%: gen_event_method_%
	:

gen_event_method_%:
	${CC} -E -P -traditional-cpp -x c ${DEFINITIONS} EventMethod.template.java | sed '/./,$$!d' > ${CLASS_NAME}.java

.PHONY: all event_method_* gen_event_method_*
