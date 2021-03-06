<%@page import="org.muffin.muffin.servlets.SessionKeys" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="m" tagdir="/WEB-INF/tags" %>
<m:base>
    <jsp:attribute
            name="title">${requestScope.cinemaBuilding.getName()}, ${requestScope.cinemaBuilding.getStreetName()}, ${requestScope.cinemaBuilding.getCity()}</jsp:attribute>
    <jsp:body>
        <m:insessioncinemabuildingownercommons>
            <jsp:attribute name="contextPath">${pageContext.request.contextPath}</jsp:attribute>
            <jsp:attribute
                    name="inSessionCinemaBuildingOwnerId">${sessionScope.get(SessionKeys.CINEMA_BUILDING_OWNER).getId()}</jsp:attribute>
            <jsp:body>
                <script type="text/babel">
                    /**
                     * @propFunctions: onDeleteClick
                     * */
                    let TheatreItem = React.createClass({
                        getInitialState: function () {
                            return {
                                inReadMode: true,
                            }
                        },
                        render: function () {
                            return (
                                    <tr title={this.props.screenNo}>
                                        <td><span className="flow-text">{this.props.screenNo}</span></td>
                                        <td>
                                            <a href={"${pageContext.request.contextPath}/cinemabuildingowner/theatreviewer?cinemaBuildingId=${requestScope.cinemaBuilding.getId()}&theatreId=" + this.props.id + "&screenNo=" + this.props.screenNo}
                                               title="Seat Layout"
                                               className="btn-floating waves-effect waves-light green">
                                                <i className="material-icons">apps</i>
                                            </a>
                                        </td>
                                        <td>
                                            <a href={"${pageContext.request.contextPath}/cinemabuildingowner/showeditor?theatreId=" + this.props.id}
                                               title="Edit Shows"
                                               className="btn-floating waves-effect waves-light blue">
                                                <i className="material-icons">local_movies</i>
                                            </a>
                                        </td>
                                        <td title="Delete">
                                            <button
                                                    onClick={(e) => {
                                                        this.props.onDeleteClick(this.props.id)
                                                    }}
                                                    className="btn-floating waves-effect waves-light red">
                                                <i className="material-icons">remove</i>
                                            </button>
                                        </td>
                                    </tr>
                            );
                        }
                    });

                    let TheatreList = React.createClass({
                        getInitialState: function () {
                            return {
                                cinemaBuildingId: ${requestScope.cinemaBuilding.getId()},
                                theatres: [
                                    <jstl:forEach items="${requestScope.theatreList}" var="theatre">
                                    {
                                        id: ${theatre.id},
                                        cinemaBuildingId: ${theatre.cinemaBuildingId},
                                        screenNo: ${theatre.screenNo},

                                    },
                                    </jstl:forEach>
                                ],
                            }
                        },
                        deleteTheatre: function (id) {
                            let self = this;
                            $.ajax({
                                url: '${pageContext.request.contextPath}/theatre/delete',
                                type: 'GET',
                                data: {id: id},
                                success: function (r) {
                                    let json = JSON.parse(r);
                                    if (json.status === -1) {
                                        Materialize.toast(json.error, 2000);
                                    }
                                    else {
                                        self.setState((prevState, props) => {
                                            let delIndex = -1;
                                            prevState.theatres.forEach((theatre, i) => {
                                                if (theatre.id === id) {
                                                    delIndex = i;
                                                }
                                            });
                                            prevState.theatres.splice(delIndex, 1);
                                            return prevState;
                                        });
                                    }
                                },
                                error: function (data) {
                                    Materialize.toast('Server Error', 2000);
                                }
                            });
                        },
                        render: function () {
                            return (
                                    <table className="highlight centered striped">
                                        <thead>
                                        <tr>
                                            <th>Screen No</th>
                                            <th></th>
                                            <th></th>
                                            <th>
                                                <a href="${pageContext.request.contextPath}/cinemabuildingowner/theatrecreator?cinemaBuildingId=${requestScope.cinemaBuilding.getId()}"
                                                   className="btn-floating waves-effect waves-light green">
                                                    <i className="material-icons">add</i>
                                                </a>
                                            </th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        {
                                            this.state.theatres.map(m => {
                                                return <TheatreItem key={m.id}
                                                                    id={m.id}
                                                                    screenNo={m.screenNo}
                                                                    onDeleteClick={this.deleteTheatre}
                                                />;
                                            })
                                        }
                                        </tbody>
                                    </table>
                            );
                        }
                    });

                    ReactDOM.render(<TheatreList/>, document.getElementById('app'));
                </script>
                <div class="container" style="min-height: 100vh">
                    <h1>${requestScope.cinemaBuilding.getName()}, ${requestScope.cinemaBuilding.getStreetName()}, ${requestScope.cinemaBuilding.getCity()}</h1>
                    <div id="app"></div>
                </div>
            </jsp:body>
        </m:insessioncinemabuildingownercommons>
    </jsp:body>
</m:base>