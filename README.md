This is a simple android app to decode "encoded" SMSs sent by alarm systems that use the "ContactID" syntax (for example Abus).
It displays SMS sent from a specific number, and decodes them according to predefined settings for:
- ContactID event codes
- user codes
- sensor codes

The ContactID syntax recognized is based on 15 numbers followed by one letter, which are:
- 0: constant (?)
- _ _ _: owner of alarm
- 18: syntax (should always be 18 for ContactID)
- _ : event kind
- _ _ _: ContactID event code
- _ _: user
- _ _ _: zone (sensor)
- _: checksum (hexa)
