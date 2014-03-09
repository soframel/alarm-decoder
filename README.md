This is a simple android app to decode "encoded" SMSs sent by alarm systems that use the "ContactID" syntax (for example Abus).
It displays SMS sent from a specific number, and decodes the ContactID according to predefined settings for:
- ContactID event codes
- Event kinds

It uses user settings to decode:
- user codes
- sensor codes

The ContactID syntax recognized is based on 15 numbers followed by one letter, which are:
- _ _ _ _: owner of alarm
- 18: syntax (should always be 18 for ContactID)
- _ : event kind
- _ _ _: ContactID event code
- _ _: user
- _ _ _: zone (sensor)
- _: checksum (hexa)
