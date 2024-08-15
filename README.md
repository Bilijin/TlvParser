# TLV Parser
This is an application that parses TLV data according to EMVco standards.

## Installation Guide
There are two options for installing this application:
<ul>
    <li>Download and install the TlvParser.apk file from the <a href="https://github.com/Bilijin/TlvParser/releases/tag/v1.0">TlvParser v1.0 release</a> of this repository</li>
    <li>Clone this repository then build and run in Android Studio or any other IDE of your choice that is capable of running and building Android projects</li>
</ul>

## Testing Guide
Paste/Type an hexadecimal string of TLV data into the input field in the home screen of the application and then press the Parse Tlv button. If the data is valid, it gets parsed and displayed in a list on the home screen. If some parts of the data are not valid, it parses and display up to the TLV value before the invalid one and displays an error message explaining what the issue is.

## Assumptions
<ul>
    <li>The application follows the standards listed out in the EMVco guidelines for parsing TLV data</li>
    <li>The tag has to be a minumum of one byte</li>
    <li>The length has to be a minumum of one byte and a maximum of two bytes</li>
    <li>The value can be of any length </li>
    <li>If the length is 0x00, the value is unset and therefore blank</li>
</ul>
