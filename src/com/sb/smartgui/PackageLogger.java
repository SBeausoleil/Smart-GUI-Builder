package com.sb.smartgui;

import java.util.logging.Logger;

public class PackageLogger {
    public static final Logger PACKAGE_LOGGER = Logger.getLogger(PackageLogger.class.getPackage().getName());
}
