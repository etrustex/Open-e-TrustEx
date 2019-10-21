/**
 * Version: MPL 1.1/EUPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at:
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is Copyright The PEPPOL project (http://www.peppol.eu)
 *
 * Alternatively, the contents of this file may be used under the
 * terms of the EUPL, Version 1.1 or - as soon they will be approved
 * by the European Commission - subsequent versions of the EUPL
 * (the "Licence"); You may not use this work except in compliance
 * with the Licence.
 * You may obtain a copy of the Licence at:
 * http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 *
 * If you wish to allow use of your version of this file only
 * under the terms of the EUPL License and not to allow others to use
 * your version of this file under the MPL, indicate your decision by
 * deleting the provisions above and replace them with the notice and
 * other provisions required by the EUPL License. If you do not delete
 * the provisions above, a recipient may use your version of this file
 * under either the MPL or the EUPL License.
 */
package eu.europa.ec.etrustex.types;


/**
 * Base interface for a single identifier issuing agency.
 * 
 * @author PEPPOL.AT, BRZ, Philip Helger
 */
public interface IIdentifierIssuingAgency {
  /**
   * Get the scheme ID of this issuing agency.<br>
   * Example for GLN <code>"GLN"</code> is returned.
   * 
   * @return The scheme ID of this issuing agency. May neither be
   *         <code>null</code> nor empty.
   */
   String getSchemeID ();

  /**
   * Get the optional name of the agency. This is pure descriptive text without
   * any predefined semantics.
   * 
   * @return The optional name of this agency. May be <code>null</code>.
   */
  String getSchemeAgency ();

  /**
   * Get the ISO-6523 based identifier value.<br>
   * Example: this method returns "0088" for GLN.
   * 
   * @return The ISO6523 based identifier of this agency. May neither be
   *         <code>null</code> nor empty. Is currently always numeric, but may
   *         contain characters in the future.
   */

  String getISO6523Code ();

  /**
   * Get the real participant identifier value for the given local identifier.<br>
   * Example: <code>GLN.createIdentifierValue ("123456")</code> results in
   * <code>0088:123456</code>
   * 
   * @param sIdentifier
   *        The local participant identifier to be used.
   * @return The participant identifier value part. Never <code>null</code>.
   */

  String createIdentifierValue (String sIdentifier);

 

  /**
   * @return <code>true</code> if the agency is deprecated and should not be
   *         used any longer, <code>false</code> otherwise.
   */
  boolean isDeprecated ();


}
