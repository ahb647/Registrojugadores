package edu.ucne.registrojugadores.ui.theme.screen.Logros

import edu.ucne.registrojugadores.Domain.Model.Model.Logros

interface LogrosEvent {

    // Eventos de cambio de campos
    data class LogroNombreChanged(val value: String) : LogrosEvent
    data class DescripcionChanged(val value: String) : LogrosEvent

    // Eventos de acciones sobre logros
    data class Save(val logro: Logros) : LogrosEvent
    data class Delete(val logro: Logros) : LogrosEvent
    data class Select(val logro: Logros) : LogrosEvent
}
