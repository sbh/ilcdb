<g:select id="attorney" name="attorney"
    from="${Attorney.findAllByIsActive(true) + (clientCase.attorney && !clientCase.attorney.isActive ? [clientCase.attorney] : [])}"
    optionKey="id"
    optionValue="firstName"
    value="${clientCase.attorney?.id}"
    noSelection="${clientCase.attorney == null ? ['null':Attorney.UNASSIGNED] : null}" />
