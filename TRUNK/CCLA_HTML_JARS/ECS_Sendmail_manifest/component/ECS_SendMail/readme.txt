ECS_SendMail Component

Description:
============

This component can be used send an email to one or more recipients. It is
now possible to attach items to emails, this needs further documentation.

Usage:
======

Execute the service ECS_SENDMAIL with the following parameters

    recipient - a comma separated list of user names or names of aliases
    email     - the email address to send the mail to

Note: use either recipient or email parameters but not both
    
    subject   - the email subject
    template  - the template to be used for the email
    debug     - writes debug code to the console
    noAliases - flag to be used if your recipient list does not contain any aliases (just speeds up processing)

Example:

http://localhost/stellent/idcplg?IdcService=ECS_SENDMAIL&recipient=sysadmin&subject=test&template=ECS_MAIL_SIMPLE&debug=y&IsJava=1