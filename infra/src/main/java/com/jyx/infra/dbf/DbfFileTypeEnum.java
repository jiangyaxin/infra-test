package com.jyx.infra.dbf;

public enum DbfFileTypeEnum {
    FOX_BASE_1(0x02, "FoxBASE"),
    FOX_BASE_PLUS_1(0x03, "FoxBASE+/Dbase III plus, no memo"),
    VISUAL_FOX_PRO_1(0x30, "Visual FoxPro"),
    VISUAL_FOX_PRO_2(0x31, "Visual FoxPro, autoincrement enabled"),
    DBASE_IV_1(0x43, "dBASE IV SQL table files, no memo"),
    DBASE_IV_2(0x63, "dBASE IV SQL system files, no memo"),
    FOX_BASE_PLUS_2(0x83, "FoxBASE+/dBASE III PLUS, with memo"),
    DBASE_IV_3(0x8B, "dBASE IV with memo"),
    DBASE_IV_4(0xCB, "dBASE IV SQL table files, with memo"),
    FOX_PRO_2X(0xF5, "FoxPro 2.x (or earlier) with memo"),
    FOX_BASE_2(0xFB, "FoxBASE"),
    DBASE_VII_1(0x44, "dBASE VII SQL table files, no memo"),
    DBASE_VII_2(0x64, "dBASE VII SQL system files, no memo"),
    DBASE_I_VII_3(0x8D, "dBASE VII with memo"),
    DBASE_I_VII_4(0xCD, "dBASE VII SQL table files, with memo");

    private final int type;
    private final String description;

    DbfFileTypeEnum(int type, String description) {
        this.type = type;
        this.description = description;
    }

    public byte toByte() {
        return (byte) type;
    }

    public static DbfFileTypeEnum fromInt(byte type) {
        int iType = 0xFF & type;
        for (DbfFileTypeEnum e : DbfFileTypeEnum.values()) {
            if (e.type == iType) {
                return e;
            }
        }
        throw DbfException.of(String.format("Unknown dbf file type:%s", type));
    }
}
