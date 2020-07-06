# ParticleEffects

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

<img align="right" width="267" height="140" src="https://github.com/4kills/ParticleEffects/blob/master/pictures/DoubleHelixParticleEffect.gif">

This library allows you to incorporate a variety of astounding visual effects into your Minecraft server plugin!  

The computations are entirely server based and highly efficient. The clients only receive the particles which ensures minimal load on their side,
to avoid driving away clients with older or lower-end hardware! 
You can also precisely control who will see which effects! 

All of the effects are highly configurable and customizable!


## Table of Contents
- [Installation](#installation)
- [Usage](#usage)
- [Attribution](#attribution)

# Installation

# Usage

To display a particle effect you just have to call the constructor of the corresponding class and the effect will display automatically. 

The following example illustrates the usage of a double helix effect that spins around a given projectile: 

```java
@EventHandler
public void onEntityShootBowEvent(final EntityShootBowEvent event) {
    // ..null-checks/instanceof omitted..
    final Projectile arrow = (Projectile) event.getProjectile();
    
    /*
      Create a new orange-colored double helix particle effect around arrow, 
      sending packets to the client from the provided plugin, with default rotation arc theta, 
      a radius around the projectile of 0.6m (blocks), a particle density of 2 (default is 1) 
      and 1.5 x default particle size.
      
      This is one of many constructor overloads that provide a variety of tweakable attributes 
      to fine-tune the effect to your needs!
    */
    new DoubleHelixParticleEffect(plugin, arrow, Color.ORANGE, 
        DoubleHelixParticleEffect.DEFAULT_THETA, 0.6, 2, 1.5f);
}
```  

Which will look like that:

![DoubleHelixParticleEffectGif](https://github.com/4kills/ParticleEffects/blob/master/pictures/DoubleHelixParticleEffect.gif)

All of the other effects are used in the same fashion and have analogous parameters. 

# Attribution

### This library uses:

- [ejml](http://ejml.org/) for fast matrix arithmetic, powering efficient calculation of particle positions. 
  
  <details> 
    
    <summary> License </summary> 
  
  ```text
  Copyright (C) Peter Abeles <peter.abeles@gmail.com>
          
                                Apache License
                           Version 2.0, January 2004
                        http://www.apache.org/licenses/

   TERMS AND CONDITIONS FOR USE, REPRODUCTION, AND DISTRIBUTION

   1. Definitions.

      "License" shall mean the terms and conditions for use, reproduction,
      and distribution as defined by Sections 1 through 9 of this document.

      "Licensor" shall mean the copyright owner or entity authorized by
      the copyright owner that is granting the License.

      "Legal Entity" shall mean the union of the acting entity and all
      other entities that control, are controlled by, or are under common
      control with that entity. For the purposes of this definition,
      "control" means (i) the power, direct or indirect, to cause the
      direction or management of such entity, whether by contract or
      otherwise, or (ii) ownership of fifty percent (50%) or more of the
      outstanding shares, or (iii) beneficial ownership of such entity.

      "You" (or "Your") shall mean an individual or Legal Entity
      exercising permissions granted by this License.

      "Source" form shall mean the preferred form for making modifications,
      including but not limited to software source code, documentation
      source, and configuration files.

      "Object" form shall mean any form resulting from mechanical
      transformation or translation of a Source form, including but
      not limited to compiled object code, generated documentation,
      and conversions to other media types.

      "Work" shall mean the work of authorship, whether in Source or
      Object form, made available under the License, as indicated by a
      copyright notice that is included in or attached to the work
      (an example is provided in the Appendix below).

      "Derivative Works" shall mean any work, whether in Source or Object
      form, that is based on (or derived from) the Work and for which the
      editorial revisions, annotations, elaborations, or other modifications
      represent, as a whole, an original work of authorship. For the purposes
      of this License, Derivative Works shall not include works that remain
      separable from, or merely link (or bind by name) to the interfaces of,
      the Work and Derivative Works thereof.

      "Contribution" shall mean any work of authorship, including
      the original version of the Work and any modifications or additions
      to that Work or Derivative Works thereof, that is intentionally
      submitted to Licensor for inclusion in the Work by the copyright owner
      or by an individual or Legal Entity authorized to submit on behalf of
      the copyright owner. For the purposes of this definition, "submitted"
      means any form of electronic, verbal, or written communication sent
      to the Licensor or its representatives, including but not limited to
      communication on electronic mailing lists, source code control systems,
      and issue tracking systems that are managed by, or on behalf of, the
      Licensor for the purpose of discussing and improving the Work, but
      excluding communication that is conspicuously marked or otherwise
      designated in writing by the copyright owner as "Not a Contribution."

      "Contributor" shall mean Licensor and any individual or Legal Entity
      on behalf of whom a Contribution has been received by Licensor and
      subsequently incorporated within the Work.

   2. Grant of Copyright License. Subject to the terms and conditions of
      this License, each Contributor hereby grants to You a perpetual,
      worldwide, non-exclusive, no-charge, royalty-free, irrevocable
      copyright license to reproduce, prepare Derivative Works of,
      publicly display, publicly perform, sublicense, and distribute the
      Work and such Derivative Works in Source or Object form.

   3. Grant of Patent License. Subject to the terms and conditions of
      this License, each Contributor hereby grants to You a perpetual,
      worldwide, non-exclusive, no-charge, royalty-free, irrevocable
      (except as stated in this section) patent license to make, have made,
      use, offer to sell, sell, import, and otherwise transfer the Work,
      where such license applies only to those patent claims licensable
      by such Contributor that are necessarily infringed by their
      Contribution(s) alone or by combination of their Contribution(s)
      with the Work to which such Contribution(s) was submitted. If You
      institute patent litigation against any entity (including a
      cross-claim or counterclaim in a lawsuit) alleging that the Work
      or a Contribution incorporated within the Work constitutes direct
      or contributory patent infringement, then any patent licenses
      granted to You under this License for that Work shall terminate
      as of the date such litigation is filed.

   4. Redistribution. You may reproduce and distribute copies of the
      Work or Derivative Works thereof in any medium, with or without
      modifications, and in Source or Object form, provided that You
      meet the following conditions:

      (a) You must give any other recipients of the Work or
          Derivative Works a copy of this License; and

      (b) You must cause any modified files to carry prominent notices
          stating that You changed the files; and

      (c) You must retain, in the Source form of any Derivative Works
          that You distribute, all copyright, patent, trademark, and
          attribution notices from the Source form of the Work,
          excluding those notices that do not pertain to any part of
          the Derivative Works; and

      (d) If the Work includes a "NOTICE" text file as part of its
          distribution, then any Derivative Works that You distribute must
          include a readable copy of the attribution notices contained
          within such NOTICE file, excluding those notices that do not
          pertain to any part of the Derivative Works, in at least one
          of the following places: within a NOTICE text file distributed
          as part of the Derivative Works; within the Source form or
          documentation, if provided along with the Derivative Works; or,
          within a display generated by the Derivative Works, if and
          wherever such third-party notices normally appear. The contents
          of the NOTICE file are for informational purposes only and
          do not modify the License. You may add Your own attribution
          notices within Derivative Works that You distribute, alongside
          or as an addendum to the NOTICE text from the Work, provided
          that such additional attribution notices cannot be construed
          as modifying the License.

      You may add Your own copyright statement to Your modifications and
      may provide additional or different license terms and conditions
      for use, reproduction, or distribution of Your modifications, or
      for any such Derivative Works as a whole, provided Your use,
      reproduction, and distribution of the Work otherwise complies with
      the conditions stated in this License.

   5. Submission of Contributions. Unless You explicitly state otherwise,
      any Contribution intentionally submitted for inclusion in the Work
      by You to the Licensor shall be under the terms and conditions of
      this License, without any additional terms or conditions.
      Notwithstanding the above, nothing herein shall supersede or modify
      the terms of any separate license agreement you may have executed
      with Licensor regarding such Contributions.

   6. Trademarks. This License does not grant permission to use the trade
      names, trademarks, service marks, or product names of the Licensor,
      except as required for reasonable and customary use in describing the
      origin of the Work and reproducing the content of the NOTICE file.

   7. Disclaimer of Warranty. Unless required by applicable law or
      agreed to in writing, Licensor provides the Work (and each
      Contributor provides its Contributions) on an "AS IS" BASIS,
      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
      implied, including, without limitation, any warranties or conditions
      of TITLE, NON-INFRINGEMENT, MERCHANTABILITY, or FITNESS FOR A
      PARTICULAR PURPOSE. You are solely responsible for determining the
      appropriateness of using or redistributing the Work and assume any
      risks associated with Your exercise of permissions under this License.

   8. Limitation of Liability. In no event and under no legal theory,
      whether in tort (including negligence), contract, or otherwise,
      unless required by applicable law (such as deliberate and grossly
      negligent acts) or agreed to in writing, shall any Contributor be
      liable to You for damages, including any direct, indirect, special,
      incidental, or consequential damages of any character arising as a
      result of this License or out of the use or inability to use the
      Work (including but not limited to damages for loss of goodwill,
      work stoppage, computer failure or malfunction, or any and all
      other commercial damages or losses), even if such Contributor
      has been advised of the possibility of such damages.

   9. Accepting Warranty or Additional Liability. While redistributing
      the Work or Derivative Works thereof, You may choose to offer,
      and charge a fee for, acceptance of support, warranty, indemnity,
      or other liability obligations and/or rights consistent with this
      License. However, in accepting such obligations, You may act only
      on Your own behalf and on Your sole responsibility, not on behalf
      of any other Contributor, and only if You agree to indemnify,
      defend, and hold each Contributor harmless for any liability
      incurred by, or claims asserted against, such Contributor by reason
      of your accepting any such warranty or additional liability.

   END OF TERMS AND CONDITIONS
  ```

  </details> 

- [PacketWrapper](https://github.com/dmulloy2/PacketWrapper) for sending packets containing particle data to the clients.

  <details>
  
  <summary> License </summary> 
  
  ```text
  Copyright (C) dmulloy2 <http://dmulloy2.net>
  Copyright (C) Kristian S. Strangeland
                       
                      GNU LESSER GENERAL PUBLIC LICENSE
                         Version 3, 29 June 2007
  
    Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
  Everyone is permitted to copy and distribute verbatim copies
  of this license document, but changing it is not allowed.
  
  
    This version of the GNU Lesser General Public License incorporates
  the terms and conditions of version 3 of the GNU General Public
  License, supplemented by the additional permissions listed below.
  
    0. Additional Definitions.
  
    As used herein, "this License" refers to version 3 of the GNU Lesser
  General Public License, and the "GNU GPL" refers to version 3 of the GNU
  General Public License.
  
    "The Library" refers to a covered work governed by this License,
  other than an Application or a Combined Work as defined below.
  
    An "Application" is any work that makes use of an interface provided
  by the Library, but which is not otherwise based on the Library.
  Defining a subclass of a class defined by the Library is deemed a mode
  of using an interface provided by the Library.
  
    A "Combined Work" is a work produced by combining or linking an
  Application with the Library.  The particular version of the Library
  with which the Combined Work was made is also called the "Linked
  Version".
  
    The "Minimal Corresponding Source" for a Combined Work means the
  Corresponding Source for the Combined Work, excluding any source code
  for portions of the Combined Work that, considered in isolation, are
  based on the Application, and not on the Linked Version.
  
    The "Corresponding Application Code" for a Combined Work means the
  object code and/or source code for the Application, including any data
  and utility programs needed for reproducing the Combined Work from the
  Application, but excluding the System Libraries of the Combined Work.
  
    1. Exception to Section 3 of the GNU GPL.

    You may convey a covered work under sections 3 and 4 of this License
  without being bound by section 3 of the GNU GPL.
  
    2. Conveying Modified Versions.
  
    If you modify a copy of the Library, and, in your modifications, a
  facility refers to a function or data to be supplied by an Application
  that uses the facility (other than as an argument passed when the
  facility is invoked), then you may convey a copy of the modified
  version:
  
    a) under this License, provided that you make a good faith effort to
    ensure that, in the event an Application does not supply the
    function or data, the facility still operates, and performs
    whatever part of its purpose remains meaningful, or
  
    b) under the GNU GPL, with none of the additional permissions of
    this License applicable to that copy.
  
    3. Object Code Incorporating Material from Library Header Files.

    The object code form of an Application may incorporate material from
  a header file that is part of the Library.  You may convey such object
  code under terms of your choice, provided that, if the incorporated
  material is not limited to numerical parameters, data structure
  layouts and accessors, or small macros, inline functions and templates
  (ten or fewer lines in length), you do both of the following:
  
    a) Give prominent notice with each copy of the object code that the
    Library is used in it and that the Library and its use are
    covered by this License.

    b) Accompany the object code with a copy of the GNU GPL and this license
    document.
  
    4. Combined Works.

    You may convey a Combined Work under terms of your choice that,
  taken together, effectively do not restrict modification of the
  portions of the Library contained in the Combined Work and reverse
  engineering for debugging such modifications, if you also do each of
  the following:
  
    a) Give prominent notice with each copy of the Combined Work that
    the Library is used in it and that the Library and its use are
    covered by this License.
  
    b) Accompany the Combined Work with a copy of the GNU GPL and this license
    document.

    c) For a Combined Work that displays copyright notices during
    execution, include the copyright notice for the Library among
    these notices, as well as a reference directing the user to the
    copies of the GNU GPL and this license document.
  
    d) Do one of the following:
  
        0) Convey the Minimal Corresponding Source under the terms of this
        License, and the Corresponding Application Code in a form
        suitable for, and under terms that permit, the user to
        recombine or relink the Application with a modified version of
        the Linked Version to produce a modified Combined Work, in the
        manner specified by section 6 of the GNU GPL for conveying
        Corresponding Source.
  
        1) Use a suitable shared library mechanism for linking with the
        Library.  A suitable mechanism is one that (a) uses at run time
        a copy of the Library already present on the user's computer
        system, and (b) will operate properly with a modified version
        of the Library that is interface-compatible with the Linked
        Version.

    e) Provide Installation Information, but only if you would otherwise
    be required to provide such information under section 6 of the
    GNU GPL, and only to the extent that such information is
    necessary to install and execute a modified version of the
    Combined Work produced by recombining or relinking the
    Application with a modified version of the Linked Version. (If
    you use option 4d0, the Installation Information must accompany
    the Minimal Corresponding Source and Corresponding Application
    Code. If you use option 4d1, you must provide the Installation
    Information in the manner specified by section 6 of the GNU GPL
    for conveying Corresponding Source.)
  
    5. Combined Libraries.
  
    You may place library facilities that are a work based on the
  Library side by side in a single library together with other library
  facilities that are not Applications and are not covered by this
  License, and convey such a combined library under terms of your
  choice, if you do both of the following:

    a) Accompany the combined library with a copy of the same work based
    on the Library, uncombined with any other library facilities,
    conveyed under the terms of this License.

    b) Give prominent notice with the combined library that part of it
    is a work based on the Library, and explaining where to find the
    accompanying uncombined form of the same work.

    6. Revised Versions of the GNU Lesser General Public License.
  
    The Free Software Foundation may publish revised and/or new versions
  of the GNU Lesser General Public License from time to time. Such new
  versions will be similar in spirit to the present version, but may
  differ in detail to address new problems or concerns.

    Each version is given a distinguishing version number. If the
  Library as you received it specifies that a certain numbered version
  of the GNU Lesser General Public License "or any later version"
  applies to it, you have the option of following the terms and
  conditions either of that published version or of any later version
  published by the Free Software Foundation. If the Library as you
  received it does not specify a version number of the GNU Lesser
  General Public License, you may choose any version of the GNU Lesser
  General Public License ever published by the Free Software Foundation.

    If the Library as you received it specifies that a proxy can decide
  whether future versions of the GNU Lesser General Public License shall
  apply, that proxy's public statement of acceptance of any version is
  permanent authorization for you to choose that version for the
  Library.
  ```
  
  </details>
  
