module core {
    requires common;
    requires hamcrest.core;
    requires util;
    requires junit3;
    requires model;
    requires notification;
    exports junit.framework;
//    exports junit.extensions;
    exports junit.textui;
    exports junit.runner;
    exports org.junit;
}