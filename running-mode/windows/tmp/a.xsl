<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <!-- Specificăm formatul de ieșire ca HTML -->
    <xsl:output method="html" indent="yes"/>

    <!-- Rădăcina transformării -->
    <xsl:template match="/carti">
        <html>
            <head>
                <title>Lista de Cărți</title>
            </head>
            <body>
                <h1>Cărți disponibile</h1>
                <ul>
                    <xsl:for-each select="carte">
                        <li>
                            <strong><xsl:value-of select="titlu"/></strong>
                            — <xsl:value-of select="autor"/>
                        </li>
                    </xsl:for-each>
                </ul>
            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>
