package org.courselab.app.org.courselab.app.models

data class MunicipioSearchBarResult(
    val comunidad_autonoma_ID : Int,
    val provincia_ID : Int,
    val municipio_ID : Int,
    val municipio : String,
)

/**
 * {
 *     "next": null,
 *     "previous": null,
 *     "current_page": 1,
 *     "update_date": "2025.01",
 *     "size": 2,
 *     "data": [
 *         {
 *             "CCOM": "10",
 *             "CPRO": "03",
 *             "CMUN": "04803",
 *             "CMUM": "048",
 *             "DMUN50": "CALLOSA DE ENSARRIÁ",
 *             "ALTERNATIVO_DMUN50": "CALLOSA D'EN SARRIÀ",
 *             "N_ENTIDADES_COLECTIVAS": null,
 *             "N_POBLACIONES": 1,
 *             "NUTS2": "ES52",
 *             "NUTS3": "ES521",
 *             "MIR": "03048"
 *         },
 *         {
 *             "CCOM": "10",
 *             "CPRO": "03",
 *             "CMUN": "04903",
 *             "CMUM": "049",
 *             "DMUN50": "CALL  OSA DE SEGURA",
 *             "ALTERNATIVO_DMUN50": "CALLOSA DE SEGURA",
 *             "N_ENTIDADES_COLECTIVAS": null,
 *             "N_POBLACIONES": 8,
 *             "NUTS2": "ES52",
 *             "NUTS3": "ES521",
 *             "MIR": "03049"
 *         }
 *     ],
 *     "warning": ""
 * }
 */
